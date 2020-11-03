#!/bin/sh
UPSTREAM=${1:-'@{u}'}
while true; do
	git fetch
	LOCAL=$(git rev-parse @)
	REMOTE=$(git rev-parse "$UPSTREAM")
	BASE=$(git merge-base @ "$UPSTREAM")
	
	if [ $LOCAL = $REMOTE ]; then
		echo "Up to date!"	
	elif [ $LOCAL = $BASE ]; then
		echo "Updating..."
		git pull
	elif [ $REMOTE = $BASE ]; then
		echo "Local changes!"
	else
		echo "Unknown"
	fi
	sleep 30
done
