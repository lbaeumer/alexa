gcloud ml-engine models list
https://cloud.google.com/ml-engine/docs/getting-started-training-prediction

## Set up local Learning

cd ~/workspace/alexa/cloudml-samples-master/census/estimator
TRAIN_DATA=$(pwd)/data/adult.data.csv
EVAL_DATA=$(pwd)/data/adult.test.csv

## Run a local trainer

MODEL_DIR=output
rm -rf $MODEL_DIR/*

gcloud ml-engine local train \
    --module-name trainer.task \
    --package-path trainer/ \
    -- \
    --train-files $TRAIN_DATA \
    --eval-files $EVAL_DATA \
    --train-steps 1000 \
    --job-dir $MODEL_DIR \
    --eval-steps 100

tensorboard --logdir=$MODEL_DIR
http://localhost:6006

## Run a local trainer in distributed mode

MODEL_DIR=output-dist
rm -rf $MODEL_DIR/*

gcloud ml-engine local train \
    --module-name trainer.task \
    --package-path trainer/ \
    --distributed \
    -- \
    --train-files $TRAIN_DATA \
    --eval-files $EVAL_DATA \
    --train-steps 1000 \
    --job-dir $MODEL_DIR

cd ~/workspace/alexa/cloudml-samples-master/census/estimator

tensorboard --logdir=$MODEL_DIR
http://localhost:6006

