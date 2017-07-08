# Todd Syndrome test

### Following cases are considered.

- Load all the data from REST API. I have created a REST API in mock server. Below you can see the API & the Model class for Patient Data.
- There will be a simple home screen with 2 button (Load patient's Data & History Data)
- Load patient's data will load data from API, compute if user have Todd Syndrome or not, Store all the data in sqlite.
- It will classify the patient based on the todd syndrom probablity (100 & 75 % - RED , 50 & 25 % - YELLOW, 0% - Green)
- On click on any row it will redirect to detail page. (All the positive todd syndrom params will be marked with checkmark).
- History Tab : It will load all the data from past browsed results. It will also show data in 2 tabs (History & Bookmarked) This will give doctor an option to filter out some patient's data based on priority & diagnosis.  There is a button at bottom to bookmark when clicked on detail. 

It can also be explanded to search by Name & sort by different params (Todd syndrom prob, Name, Age)

### Here are all the dependecies


- Mock API URL : https://private-f29168-patienttest1.apiary-mock.com/patients<br />

This is the reference model class for Patient. 
I have added Array for the patient_disorders & patient_drugs as there are chances that normally patient have one or more disorder
& drugs. Also i have used that as a inner object because it could be the case that Severity & Date could also be considered at later stage, so it can be extended without making any changes in the whole structure. 


{
		"patient_id": 1201,
		"patient_name": "Kangana Ranaut",
		"patient_age": 28,
		"patient_sex": "FEMALE",
		"patient_contact": "+917069248346",
		"patient_date_created": 1496707200,
		"patient_date_updated": 1499490390,
		"patient_disorders": [{
			"name": "Skin allergy",
			"severity": "MEDIUM"
		}, {
			"name": "Eyesight",
			"severity": "LOW"
		}, {
			"name": "Cold & Flu",
			"severity": "MEDIUM"
		}],
		"patient_drugs": [{
			"drug_name": "Paracetamol",
			"dosage": 6,
			"drug_date_updated": 1496707200
		}]
	}

- Quick explanation of the fields i modelled which are not used in UI

- patient_contact : Later we can just put a short button to call directly if syndrome chances are high
- patient_date_created & patient_date_updated : To allow some easy way of sorting. 
- patient_disorders >> severity : If weight is readjusted based on severity
- patient_drugs >>drug_date_updated : If weight is readjusted based on time when drug was taken for exmaple in last 30 days etc.

