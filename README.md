# KrishiNetra – AI-Powered Crop Disease Detection & Advisory System

## Overview

KrishiNetra is an AI-powered mobile application designed to help farmers detect crop diseases early using a simple photograph of a leaf. By combining computer vision, machine learning, and a mobile interface, the system analyzes plant images and provides disease identification along with treatment recommendations.

The goal of the project is to reduce crop losses caused by late disease detection and make agricultural advisory tools accessible even to farmers with limited technical knowledge.

This system focuses on creating a practical, scalable solution that can work in real agricultural environments through a mobile app connected to an AI inference backend.

---

## Problem Statement

A significant percentage of crop loss occurs because farmers identify plant diseases too late. Many existing agricultural tools:

• require expert knowledge
• are not available in regional languages
• lack real-time disease detection capability

Small farmers often rely on manual inspection or delayed agricultural consultation, which allows diseases to spread before treatment can be applied.

KrishiNetra aims to solve this by providing **instant AI-based crop diagnosis using a smartphone camera**.

---

## Proposed Solution

KrishiNetra enables farmers to:

1. Capture a photo of a crop leaf using a mobile phone
2. Upload the image to an AI backend server
3. Run a trained convolutional neural network (CNN) to detect disease
4. Receive the predicted disease name and treatment advice

The system is designed so that the mobile application communicates with a backend API, which runs the AI model and returns results.

---

## Key Features

### AI-Based Disease Detection

A deep learning model trained on agricultural image datasets detects diseases in crop leaves with high accuracy.

### Multi-Crop Support

The initial model supports multiple crops including:

• Tomato
• Potato
• Pepper (Bell Pepper)

The architecture allows additional crops to be integrated through retraining.

### Mobile-First Interface

The system is built as an Android application, allowing farmers to simply take a picture and receive results instantly.

### Fast Prediction

The AI model processes leaf images and returns predictions within a few seconds.

### Scalable Architecture

The backend system is designed so that new disease datasets can be added and the model retrained to support more crops in the future.

---

## System Architecture

The system consists of four main components:

Android Mobile App
→ captures leaf image

FastAPI Backend Server
→ receives image via API

Deep Learning Model (TensorFlow CNN)
→ performs disease classification

Prediction Response
→ sent back to the mobile application

---

## Technology Stack

Frontend
Android (Kotlin + XML)

Backend
Python – FastAPI

Machine Learning
TensorFlow / Keras

Dataset
PlantVillage Crop Disease Dataset

Model Architecture
Transfer Learning using MobileNetV2

Deployment (planned)
Render / Railway / Cloud server

Database (future scope)
Firebase

---

## Dataset

The model is trained using the PlantVillage dataset containing labeled crop disease images.

The current system supports the following classes:

Pepper – Bacterial Spot
Pepper – Healthy

Potato – Early Blight
Potato – Late Blight
Potato – Healthy

Tomato – Early Blight
Tomato – Late Blight
Tomato – Healthy

Images are resized and normalized before being used for training the CNN model.

---

## Machine Learning Approach

KrishiNetra uses **transfer learning** with a pre-trained convolutional neural network.

Steps involved:

1. Load labeled crop leaf dataset
2. Preprocess images (resize, normalize)
3. Use MobileNetV2 as the base feature extractor
4. Train classification layers on crop disease images
5. Export the trained model for inference

This approach reduces training time and improves model accuracy.

---

## Project Workflow

1. Farmer captures crop leaf image
2. Mobile app uploads image to backend API
3. Backend preprocesses image
4. CNN model predicts disease class
5. Prediction is returned to the mobile app
6. App displays disease name and advisory

---

## Uniqueness of the Project

KrishiNetra is not just a disease classifier. The system is designed to evolve into a **complete AI agricultural assistant**.

Unique aspects include:

• Multi-crop disease detection using deep learning
• Mobile-based diagnosis for accessibility
• Modular architecture for easy expansion
• Potential integration with agricultural advisory systems
• Capability to add multilingual support for farmers

Unlike many student projects that only demonstrate classification, KrishiNetra focuses on building a **practical end-to-end system combining AI and mobile technology**.

---

## Future Enhancements

Planned improvements include:

• Multilingual advisory using AI language models
• Offline disease detection support
• Yield loss prediction
• Integration with agricultural help centers
• Insurance claim photo documentation support
• Expansion to more crops and diseases

---

## Expected Impact

By enabling early disease detection and quick advisory, KrishiNetra can help farmers:

• reduce crop loss
• identify diseases earlier
• take timely treatment actions
• access agricultural assistance faster

The long-term goal is to create a scalable AI platform that supports farmers with real-time crop health insights.

---

## Project Status

Current Development Phase:
AI model training and backend API development

Next Steps:

• integrate AI model with FastAPI backend
• develop Android mobile application
• connect mobile app with backend prediction API
