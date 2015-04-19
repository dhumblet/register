#!bin/bash/
echo kassa.war deploy to openshift
echo "Did you adjust the database settings for production? (y/n)"
read  -p "y/n" mainmenuinput -n 1
if [ "$mainmenuinput" = "y" ]; then
    cp /Users/derkhumblet/coding/projects/personal/cashregister/webapp/target/kassa.war /Users/derkhumblet/coding/projects/personal/cashregister/rhc-git/register/webapps/kassa.war
    cp /Users/derkhumblet/coding/projects/personal/cashregister/webapp/target/kassa.war /Users/derkhumblet/Documents/Kassa_WARs/kassa_$(date +%Y%m%d)_$(date +%H%M)_STABLE.war
    cd /Users/derkhumblet/coding/projects/personal/cashregister/rhc-git/register/webapps
    git commit . -m "deploying new version"
    git push --force origin master
else
    exit;
fi
