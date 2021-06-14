mv ./log/demon/demon.out ./log/demon/demon.out.`date +%Y%m%d_%H%M%S`
nohup ./startBtsDemon.sh prod > ./log/demon/demon.out &
