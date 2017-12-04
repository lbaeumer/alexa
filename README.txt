
# Prediction

## deploy new version

cd ~/workspace/alexa
mvn clean install appengine:devserver
mvn clean install appengine:update

http://2.luitest123.appspot.com

# Learning

https://cloud.google.com/ml-engine/docs/getting-started-training-prediction

cd ~/workspace/alexa/cloudml-samples-master/census/estimator
TRAIN_DATA=$(pwd)/data/adult.data.csv
EVAL_DATA=$(pwd)/data/adult.test.csv

## run local trainer

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
    

python -m tensorflow.tensorboard --logdir=$MODEL_DIR

## run distributed

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


PROJECT_ID=$(gcloud config list project --format "value(core.project)")
BUCKET_NAME=${PROJECT_ID}-mlengine
