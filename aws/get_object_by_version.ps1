# Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
# .\get_object_by_version.ps1 -bucket "oleksiikushch2" -key "text2.txt" -given_date "2025-01-01" -profile "main"

param (
    [Parameter(Mandatory=$true)]
    [string]$bucket,

    [Parameter(Mandatory=$true)]
    [string]$key,

    [Parameter(Mandatory=$true)]
    [string]$given_date,

    [Parameter(Mandatory=$true)]
    [string]$profile
)

$versions = aws s3api list-object-versions --bucket $bucket --prefix $key --profile $profile | ConvertFrom-Json

$latest_version = ($versions.Versions | Where-Object { ($_.IsLatest -eq $true) -and ($_.LastModified -lt $given_date) }).VersionId

if ($latest_version) {
    aws s3api get-object --bucket $bucket --key $key --version-id $latest_version "$key" --profile main
    echo "Downloaded $key with version $latest_version"
} else {
    echo "No versions of $key older than $given_date found"
}