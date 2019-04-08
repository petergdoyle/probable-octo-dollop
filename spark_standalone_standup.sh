#!/usr/bin/env bash

target='/spark/checkpoint'

if find "$target" -mindepth 1 -print -quit 2>/dev/null | grep -q .; then
  prompt="The Spark checkpoint directory is not empty. Do you want to delete it? (y/n): "
  default_value="y"
  read -e -p "$(echo -e $prompt)" -i $default_value response
  if [ "$response" == 'y' ]; then
    sudo rm -frv /spark/checkpoint/*
  fi
fi

./spark_standalone_start_all.sh 
