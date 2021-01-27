#######################################
# Calculates the next version number
# Arguments:
#   $1 - the current version number
#   $2 - which digit to bump, one of
#     'major', 'minor', or 'patch'
# Returns:
#   The incremented version number
#######################################
function calculate_next_version {
    IFS='.' read -ra array <<< "$1"
    major=${array[0]}
    minor=${array[1]}
    patch=${array[2]}

    # Update numbers accordingly
    if [[ $2 == "major" ]]; then
        major=$(($major+1))
        minor=0
        patch=0
    elif [[ $2 == "minor" ]]; then
        minor=$(($minor+1))
        patch=0
    elif [[ $2 == "patch" ]]; then
        patch=$(($patch+1))
    else
        echo "ERROR: Please specify \$BUMP as one of: 'major', 'minor', 'patch'"
        exit 1
    fi
    echo "$major.$minor.$patch"
}

#############################################
# Wait for an app to be reachable
# Arguments:
#   $1 - url to poll, defaults to
#        http://localhost:8080 if not provided
# Globals:
#   None
# Returns:
#   None
#############################################
function wait_for_app {
  app_url=$1
  if [ -z $app_url ]; then
    app_url="http://localhost:8080"
  fi
  attempt=0
  while [ $attempt -le 24 ]; do
    attempt=$(( $attempt + 1 ))
    if curl -s -o /dev/null $app_url; then
        echo "$(date) - App is upp!"
        return
    else
        sleep 5
    fi
  done
  echo "ERROR: App failed to start in under 2 minutes!"
  exit 1
}
