param(
    [ValidateSet("exe", "app-image")]
    [string]$PackageType = "exe"
)

$ErrorActionPreference = "Stop"

function Invoke-Checked {
    param(
        [string]$Description,
        [scriptblock]$Command
    )

    Write-Host $Description
    & $Command
    if ($LASTEXITCODE -ne 0) {
        throw "$Description failed with exit code $LASTEXITCODE"
    }
}

function Test-WixInstalled {
    $wixSingle = Get-Command wix -ErrorAction SilentlyContinue
    $light = Get-Command light -ErrorAction SilentlyContinue
    $candle = Get-Command candle -ErrorAction SilentlyContinue
    return ($null -ne $wixSingle) -or (($null -ne $light) -and ($null -ne $candle))
}

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

$buildDir = Join-Path $root "build"
$classesDir = Join-Path $buildDir "classes"
$inputDir = Join-Path $buildDir "input"
$distDir = Join-Path $root "dist"
$jarPath = Join-Path $inputDir "Minhaj-Mario.jar"
$manifestPath = Join-Path $buildDir "manifest.txt"

Write-Host "Cleaning previous build artifacts..."
if (Test-Path $buildDir) { Remove-Item $buildDir -Recurse -Force }
New-Item -ItemType Directory -Path $classesDir | Out-Null
New-Item -ItemType Directory -Path $inputDir | Out-Null
if (-not (Test-Path $distDir)) { New-Item -ItemType Directory -Path $distDir | Out-Null }

Invoke-Checked "Compiling Java source files..." { javac -d $classesDir source\*.java }

Write-Host "Creating runnable JAR..."
@" 
Main-Class: GameEngine
"@.Trim() | Set-Content -Path $manifestPath -NoNewline
Invoke-Checked "Packaging JAR..." { jar --create --file $jarPath --manifest $manifestPath -C $classesDir . }

Write-Host "Copying runtime assets (images/maps)..."
Copy-Item -Path (Join-Path $root "images") -Destination $inputDir -Recurse -Force
Copy-Item -Path (Join-Path $root "maps") -Destination $inputDir -Recurse -Force

if ($PackageType -eq "exe" -and -not (Test-WixInstalled)) {
    throw "WiX Toolset is not installed or not on PATH. Install WiX (v3+) and rerun this script, or use -PackageType app-image for a portable EXE."
}

Invoke-Checked "Building Windows package ($PackageType) with jpackage..." {
    $jpackageArgs = @(
        "--type", $PackageType,
        "--name", "Minhaj-Mario",
        "--dest", $distDir,
        "--input", $inputDir,
        "--main-jar", "Minhaj-Mario.jar",
        "--main-class", "GameEngine",
        "--app-version", "1.0.0",
        "--vendor", "minhajMahmud"
    )

    if ($PackageType -eq "exe") {
        $jpackageArgs += @("--win-menu", "--win-shortcut")
    }

    jpackage @jpackageArgs
}

Write-Host "Done. Package created under: $distDir"
