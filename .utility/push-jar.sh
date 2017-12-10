#!/usr/bin/env bash

if [ "$TRAVIS_REPO_SLUG" == "ChatTriggers/ct.js" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing jar...\n"


  cp -R "build/libs" "$HOME/built-jars"

  cd $HOME

  curl --ftp-create-dirs -T built-jars -u "$FTP_USER:$FTP_PASSWORD" "ftp://chattriggers.com/public_html/jars/"

  echo -e "Published jars to prod.\n"

fi