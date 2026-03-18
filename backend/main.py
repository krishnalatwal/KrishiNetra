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
    "Tomato Early Blight": {
        "en": "Use Mancozeb fungicide and remove infected leaves.",
        "hi": "मैनकोजेब फफूंदनाशक का उपयोग करें और संक्रमित पत्तियां हटा दें।"
    },
    "Tomato Late Blight": {
        "en": "Apply copper-based fungicide and improve airflow.",
        "hi": "कॉपर आधारित फफूंदनाशक का छिड़काव करें और वायु प्रवाह बढ़ाएं।"
    },
    "Tomato Healthy": {
        "en": "No disease detected. Maintain proper irrigation.",
        "hi": "कोई बीमारी नहीं है। नियमित सिंचाई बनाए रखें।"
    },
    "Potato Early Blight": {
        "en": "Use chlorothalonil fungicide and crop rotation.",
        "hi": "क्लोरोथैलोनिल फफूंदनाशक का उपयोग करें और फसल चक्र अपनाएं।"
    },
    "Potato Late Blight": {
        "en": "Use metalaxyl fungicide and avoid leaf wetness.",
        "hi": "मेटालैक्सिल फफूंदनाशक का उपयोग करें और पत्तियों को गीला न रखें।"
    },
    "Potato Healthy": {
        "en": "Crop is healthy.",
        "hi": "फसल स्वस्थ है।"
    },
    "Pepper Bacterial Spot": {
        "en": "Use copper spray and remove infected plants.",
        "hi": "कॉपर स्प्रे का उपयोग करें और संक्रमित पौधों को हटा दें।"
    },
    "Pepper Healthy": {
        "en": "Crop is healthy.",
        "hi": "फसल स्वस्थ है।"
    }
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
async def predict(file: UploadFile = File(...), lang: str = "en"):

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

        treatment = TREATMENTS.get(disease, {}).get(lang, "No treatment info available")

        results.append({
            "disease": disease,
            "confidence": float(predictions[idx] * 100),
            "treatment": treatment
        })

    return {
        "predictions": results
    }