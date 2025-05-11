$fontUrls = @{
    "montserrat_bold.ttf" = "https://fonts.gstatic.com/s/montserrat/v25/JTUHjIg1_i6t8kCHKm4532VJOt5-QNFgpCtr6Hw5aXp-p7K4KLg.ttf"
    "montserrat_semibold.ttf" = "https://fonts.gstatic.com/s/montserrat/v25/JTUHjIg1_i6t8kCHKm4532VJOt5-QNFgpCuM73w5aXp-p7K4KLg.ttf"
    "opensans_regular.ttf" = "https://fonts.gstatic.com/s/opensans/v35/memSYaGs126MiZpBA-UvWbX2vVnXBbObj2OVZyOOSr4dVJWUgsjZ0B4gaVI.woff2"
}

$fontDir = "app/src/main/res/font"

# Create font directory if it doesn't exist
if (-not (Test-Path $fontDir)) {
    New-Item -ItemType Directory -Path $fontDir -Force
}

foreach ($font in $fontUrls.GetEnumerator()) {
    $outputPath = Join-Path $fontDir $font.Key
    Write-Host "Downloading $($font.Key)..."
    
    try {
        $webClient = New-Object System.Net.WebClient
        $webClient.Headers.Add("User-Agent", "Mozilla/5.0")
        $webClient.DownloadFile($font.Value, $outputPath)
        Write-Host "Successfully downloaded $($font.Key)"
    }
    catch {
        Write-Host "Failed to download $($font.Key): $_"
        Write-Host "Please download manually from: $($font.Value)"
    }
}

Write-Host "Font download complete!" 