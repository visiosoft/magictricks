$fonts = @{
    "montserrat_bold" = "https://fonts.gstatic.com/s/montserrat/v25/JTUHjIg1_i6t8kCHKm4532VJOt5-QNFgpCtr6Hw5aXp-p7K4KLg.ttf"
    "montserrat_semibold" = "https://fonts.gstatic.com/s/montserrat/v25/JTUHjIg1_i6t8kCHKm4532VJOt5-QNFgpCuM73w5aXp-p7K4KLg.ttf"
    "opensans_bold" = "https://fonts.gstatic.com/s/opensans/v34/memSYaGs126MiZpBA-UvWbX2vVnXBbObj2OVZyOOSr4dVJWUgsg-1y4t.ttf"
}

# Create raw directory if it doesn't exist
$rawDir = "app/src/main/res/raw"
if (-not (Test-Path $rawDir)) {
    New-Item -ItemType Directory -Path $rawDir -Force
}

# Download each font
foreach ($font in $fonts.GetEnumerator()) {
    $outputPath = Join-Path $rawDir "$($font.Key).ttf"
    Write-Host "Downloading $($font.Key)..."
    try {
        Invoke-WebRequest -Uri $font.Value -OutFile $outputPath
        Write-Host "Successfully downloaded $($font.Key)"
    } catch {
        Write-Host "Failed to download $($font.Key): $_"
        Write-Host "Please download manually from: $($font.Value)"
    }
}

Write-Host "Font download complete!" 