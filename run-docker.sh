#docker build -t nunofern/filesigner .

docker run --name=filesigner --mount type=bind,source=/home/nunomsf/projects/nunofern/config/,target=/opt/filesigner/config/ --mount type=bind,source=/home/nunomsf/projects/nunofern/share/,target=/opt/filesigner/share/ nunofern/filesigner
