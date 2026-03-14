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

TREATMENTS = {
    "Tomato Early Blight": "Use Mancozeb fungicide. Remove infected leaves and avoid overhead irrigation.",
    "Tomato Late Blight": "Apply copper-based fungicide and improve airflow between plants.",
    "Tomato Healthy": "No disease detected. Maintain regular irrigation and monitor plants.",
    
    "Potato Early Blight": "Use chlorothalonil fungicide and practice crop rotation.",
    "Potato Late Blight": "Use metalaxyl fungicide and avoid prolonged leaf wetness.",
    "Potato Healthy": "Crop is healthy. Continue regular monitoring.",
    
    "Pepper Bacterial Spot": "Use copper spray and remove infected plants to prevent spread.",
    "Pepper Healthy": "Crop is healthy. Maintain good irrigation practices."
}

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

    predictions = model.predict(image_array)[0]

    top3_idx = predictions.argsort()[-3:][::-1]

    results = []

    for idx in top3_idx:
        disease = class_names[idx]

        results.append({
            "disease": disease,
            "confidence": float(predictions[idx] * 100),
            "treatment": TREATMENTS.get(disease, "No treatment info available")
        })

    return {
        "predictions": results
    }