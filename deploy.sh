git add *
git commit
git push origin master
git push bb master
git push rs master
mvn clean install
mvn deploy -P release
mvn clean
