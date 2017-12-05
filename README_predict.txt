REGION=us-central1
PROJECT_ID=$(gcloud config list project --format "value(core.project)")
BUCKET_NAME=${PROJECT_ID}-mlengine

## Deploy a model to support prediction

MODEL_NAME=census
gcloud ml-engine models create $MODEL_NAME --regions=$REGION

OUTPUT_PATH=gs://$BUCKET_NAME/census_dist_1
gsutil ls -r $OUTPUT_PATH/export

MODEL_BINARIES=gs://$BUCKET_NAME/census_dist_1/export/Servo/1487877383942/

gcloud ml-engine versions create v1 \
--model $MODEL_NAME \
--origin $MODEL_BINARIES \
--runtime-version 1.2

gcloud ml-engine models list

gcloud ml-engine predict \
--model $MODEL_NAME \
--version v1 \
--json-instances \
../test.json



