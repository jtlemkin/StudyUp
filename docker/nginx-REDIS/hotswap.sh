#!/usr/bin/env bash
set -e

if grep -Fq "$1" /etc/nginx/nginx.conf
then
        echo "server is already located at $1"
fi

sed -i'' "s/(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/$1/" /etc/nginx/nginx.conf

/usr/sbin/nginx -s reload
