import sys
import pickle
import os
import warnings
warnings.filterwarnings("ignore")

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
model_path = os.path.join(BASE_DIR, "model", "model_diabetes.pkl")

with open(model_path, "rb") as f:
    model = pickle.load(f)

args = sys.argv[1:]
if len(args) != 8:
    print("ERROR: Butuh 8 argumen: Pregnancies Glucose BloodPressure SkinThickness Insulin BMI DiabetesPedigreeFunction Age")
    sys.exit(1)

features = [float(a) for a in args]
prediction = model.predict([features])[0]
probability = model.predict_proba([features])[0][1]

if prediction == 1:
    print(f"RISIKO DIABETES TINGGI|{probability:.2f}")
else:
    print(f"RISIKO DIABETES RENDAH|{probability:.2f}")
