#!/bin/bash

validate_is_blank()
{
  if [[ -z "$1" ]];
  then
    echo "$1 cannot be blank";
    exit 1;
  fi
}


validate_is_blank $1;
validate_is_blank $2;

PASSWORD=$1;
PLAIN_TEXT=$2;

mvn jasypt:encrypt-value -Djasypt.encryptor.password="$1" -Djasypt.plugin.value="$2";
