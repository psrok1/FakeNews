#!/bin/sh
( cd classifier &&
  pip3 install -r requirements.txt &&
  python3 install.py ) &&
(
  gradle wrapper &&
  sudo chmod +x gradlew
  ./gradlew webpack &&
  ./gradlew build
) && echo "Installed successfully!"

