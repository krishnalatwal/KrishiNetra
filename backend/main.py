from fastapi import FastAPI, UploadFile, File
from PIL import Image
import numpy as np
import tensorflow as tf
import io

app = FastAPI()

model = None

class_names = [
    "Pepper Bacterial Spot",
    "Pepper Healthy",
    "Potato Early Blight",
    "Potato Late Blight",
    "Potato Healthy",
    "Tomato Early Blight",
    "Tomato Late Blight",
    "Tomato Healthy"
]

try:
    model = tf.keras.models.load_model("model/crop_disease_model.h5")
    print("Model loaded successfully")
except:
    print("Model not found yet. Prediction will be disabled.")


@app.get("/")
def home():
    return {"message": "KrishiNetra API running"}


@app.post("/predict")
async def predict(file: UploadFile = File(...)):

    image_bytes = await file.read()

    image = Image.open(io.BytesIO(image_bytes))
    image = image.resize((224,224))

    image_array = np.array(image) / 255.0
    image_array = np.expand_dims(image_array, axis=0)

    if model is None:
        return {"error": "Model not trained yet"}

    predictions = model.predict(image_array)

    predicted_class = class_names[np.argmax(predictions)]

    confidence = float(np.max(predictions))

    return {
        "prediction": predicted_class,
        "confidence": confidence
    }