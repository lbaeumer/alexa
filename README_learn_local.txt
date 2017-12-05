gcloud ml-engine models list
https://cloud.google.com/ml-engine/docs/getting-started-training-prediction

## Set up local Learning

cd ~/workspace/alexa/cloudml-samples-master/census/estimator
TRAIN_DATA=$(pwd)/data/adult.data.csv
EVAL_DATA=$(pwd)/data/adult.test.csv

## Run a local trainer

OUTPUT_PATH=output
rm -rf $OUTPUT_PATH/*

gcloud ml-engine local train \
    --module-name trainer.task \
    --package-path trainer/ \
    -- \
    --train-files $TRAIN_DATA \
    --eval-files $EVAL_DATA \
    --train-steps 1000 \
    --job-dir $OUTPUT_PATH \
    --eval-steps 100

tensorboard --logdir=$OUTPUT_PATH
http://localhost:6006

## Run a local trainer in distributed mode

OUTPUT_PATH=output-dist
rm -rf $OUTPUT_PATH/*

gcloud ml-engine local train \
    --module-name trainer.task \
    --package-path trainer/ \
    --distributed \
    -- \
    --train-files $TRAIN_DATA \
    --eval-files $EVAL_DATA \
    --train-steps 1000 \
    --job-dir $OUTPUT_PATH

tensorboard --logdir=$OUTPUT_PATH
http://localhost:6006

