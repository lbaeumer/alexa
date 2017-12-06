REGION=us-central1
BUCKET_NAME=luitest123-mlengine
MODEL_NAME=census

## Deploy a model to support prediction

gcloud ml-engine models create $MODEL_NAME --regions=$REGION

OUTPUT_PATH=gs://$BUCKET_NAME/census_dist_1
gsutil ls -r $OUTPUT_PATH/export

MODEL_BINARIES=gs://$BUCKET_NAME/census_single_1_1/export/Servo/1512508883/

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
