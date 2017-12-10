#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "ChatTriggers/ct.js" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing userdocs...\n"

  cp -R "build/docs/javadoc" "$HOME/javadocs"

  cd $HOME

  # FIX THIS SHIT MY DUDE
  curl --ftp-create-dirs -T built-jars -u "$FTP_USER:$FTP_PASSWORD" "ftp://chattriggers.com/public_html/jars/"

  echo -e "Published userdocs to prod.\n"

fi