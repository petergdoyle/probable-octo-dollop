#!/usr/bin/env bash

./spark_standalone_stop_all.sh
echo "cleaning up checkpoint directory..."
sleep 1
sudo rm -frv /spark/checkpoint/*