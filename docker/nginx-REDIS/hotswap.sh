#!/usr/bin/env bash
set -e

if grep -Fq "$1" /etc/nginx/nginx.conf
then
        echo "server is already located at $1"
fi

sed -i'' "s/34.73.56.224/$1/" /etc/nginx/nginx.conf
sed -i'' "s/127.0.0.1/$1/" /etc/nginx/nginx.conf

/usr/sbin/nginx -s reload
