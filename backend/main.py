from fastapi import FastAPI, UploadFile, File
from PIL import Image
import io

app = FastAPI()

@app.get("/")
def home():
    return {"message": "KrishiNetra API running"}

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    
    image_bytes = await file.read()
    image = Image.open(io.BytesIO(image_bytes))

    return {
        "filename": file.filename,
        "message": "Image received successfully"
    }