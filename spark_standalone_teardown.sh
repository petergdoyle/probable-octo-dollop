#!/usr/bin/env bash


./spark_standalone_stop_worker.sh
./spark_standalone_stop_master.sh

if find "$target" -mindepth 1 -print -quit 2>/dev/null | grep -q .; then
  prompt="The Spark checkpoint directory is not empty. Do you want to delete it? (y/n): "
  default_value="y"
  read -e -p "$(echo -e $prompt)" -i $default_value response
  if [ "$response" == 'y' ]; then
    sudo rm -frv /spark/checkpoint/*
  fi
fi
