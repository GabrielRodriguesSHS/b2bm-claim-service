```mermaid
erDiagram
    
    PROCID {
%% Data comes from ServiceBench (manual configuration to get and upload)
        int proc_id PK "procId, Service, Product, Third Party ID"
        
        string code "Unique"
        string description "THD_PTY_DS"
        string coverage_code "CVG_CD"
    }
    
	CUSTOMER {
%% Data comes from SOM (XML File - ServiceOrderData)
		int customer_id PK "SVC_CUS_ID_NO"
		
		string name "CUS_NM"
		string type "CN_CUST_TYPE"
		string address "CUS_ADR"
		string city_name "CUS_CTY_NM"
		string state_code "CUS_ST_CD"
		string zip_code "ZIP_CD"
		string zip_suffix_code "ZIP_SUF_CD"
		string phone_number "CUS_PHN_NO"
	}
	
	MERCHANDISE {
%% Data comes from SOM (XML File - ServiceOrderData)
		int mechandise_id PK "Generated - Appliance, Product"
		
		string code "Unique - MDS_CD"
		string model_number "MDL_NO"
		string serial_number "SRL_NO"
		string brand_name "BND_NM"
		int item_number "ITM_NO"
		int item_suffix_number "ITM_SUF_NO"
		string code_description "MD_SM_CODE_DESC"
		string speciality "MD_MDSE_SPECIALTY"
	}
    
    SERVICE_ORDER {
%% Data comes from SOM (XML File - ServiceOrderData)
        int service_order_id PK
        int proc_id FK "procId - third_party_id"
        int merchandise_id FK "generated"
        int customer_id FK "SVC_CUS_ID_NO"
		
        int unit_number "SVC_UN_NO"
		int order_number "SO_NO"
		date created_date "SO_CRT_DT"
		date closed_date "SO_CL_DT"
		string status "SO_STS_CD"
		int employee_number "SO_CRT_EMP_NO"
		int created_unit_number "SO_CRT_UN_NO"
		int contract_agreement_number "CTA_NO"
		string authorization_number "ATH_NO"
		string instruction_1 "SO_INS_1_DS"
		string instruction_2 "SO_INS_2_DS"
		%% int last_attempt_employee_number "ServiceAttempt.SVC_TEC_EMP_NO"
		string origin_third_party_id "Ori_thd_pty_id"
		string request_description "SVC_RQ_DS"
		string recall_flag "SVC_ORD_RCL_FL"
		int location_suffix_number "LOC_SUF_NO"
		string origin_program_code "SVC_OGP_CD"
		string type_code "SO_TYP_CD"
		
        date parts_warranty_expiration_date "PRT_WAR_EPR_DT"
		date labor_warranty_expiration_date "LAB_WAR_EPR_DT"
		double part_tax_rate "PART_SO_TAX_RT"
		double labor_tax_rate "LAB_SO_TAX_RT"
		
        date merchandise_purchase_date "CUS_PUR_DT"
		date merchandise_delivery_date "CUS_DVR_DT"
		
%% Still questions about those
		%% string combined_third_party_id "double-check with Shankar if this is the same as third_party_description"
		%%string organization "check with Shankar where to get this data from"
		%% string kcd_brand "check with Shankar if this is the same as brand_name"
    }
    
    PART {
%% Data comes from SOM (XML File - Parts)
        int part_id PK
        int service_order_id FK
        
        int sequence_number "PRT_SEQ_NO"
%% These 3 are used for querying the ItemMasterAPI
        int service_division_number "SVC_DIV_NO"
        int source_number "PRT_PRC_LIS_SRC_NO"
        string number "SVC_PRT_NO"
%% These 3 are used for querying the ItemMasterAPI
        
        string description "PRT_DS"
        int service_attempt_part_quantity "SVC_ATP_PRT_QT"
        string coverage_code "CVG_CODE_PARTS"
        string status "SVC_PRT_STS_CD"
        string type_code "SVC_PRT_TYP_CD"
        date order_date "PRT_ORD_DT"
        date installed_date "PRT_INS_DT"
        string obligor_key "OBL_KEY"
        
        double cost_amount "comes from ItemMasterAPI"
        double sell_price_amount "comes from ItemMasterAPI"
        
        double expected_revenue_amount "calculated via P3 Table"
        double price_multiplier "calculated via P3 Table"
        %% string price_flag "calculated via P3 Table - check with Shankar if this is the same as the Location such as L = 0.7 ratio"
        
        double tax_rate "PRT_SO_TAX_RT"
        double tax_amount "part_tax_rate * part_sell_price_amount"
        
        string tracking_information "PsDummy5"
        double shipping_amount "PSDummy4"
    }
    
    LABOR_JOB_CODE {
%% Data comes from SOM (XML File - JobCode)
        int labor_job_code_id PK
        int service_order_id FK
        
        int job_code "JOB_CD, Might be FK in the future"
        int sequence_number "JOB_CD_SEQ_NO"
        string job_charge_code "JOB_CD_CRG_CD"
        string job_description "JOB_CD_DS"
        date call_date "SVC_CAL_DT"
        string coverage_code "CVG_CD or Warranty"
        string type "CRG_CD_NREL_FL"
        
        double requested_amount "calculated via P3 and JE Tables"
        double tax_amount "calculated via P3 and JE Tables"
        
        double miscellaneous_amount "unused for now"
        double refrigerant_cost_amount "unused for now"
        double fuel_surcharge_amount "unused for now"
    }
        
    SERVICE_ATTEMPT {
%% Data comes from SOM (XML File - ServiceAttempt)
        int service_attempt_id PK
        int service_order_id FK
        
        date date "SVC_CAL_DTS"
        int sequence_number "attemptCount"
        int call_code "SVC_CAL_CD"
        int technician_employee_number "SVC_TEC_EMP_NO"
        string technician_comment_1 "SVC_ATP_CMT_1_DS"
        string technician_comment_2 "SVC_ATP_CMT_2_DS"
        datetime technician_arrival_time "SVC_ATP_TEC_ARV_TM"
        datetime technician_departure_time "SVC_ATP_END_CAL_TM"
        int service_time_minutes "TST_TM_QT"
    }
    
    CLAIM {
%% Data is generated by us
        int claim_id PK
        
        string number
        %% string reference_number
        date date
        %%[CREATED, VALIDATING, VALIDATED, INVALID, REJECTED, SENT_TO_PARTNER, APPROVED, FULLY PAID, PARTIALLY PAID, CLOSED]
        string status
        string status_reason

        %% requested amounts
        date submission_date
        double part_requested_amount
        double part_tax_request_amount
        double part_shipping_requested_amount
        double labor_requested_amount
        double labor_tax_request_amount
        double refrigerant_requested_amount
        double miscellaneous_requested_amount
        double fuel_surcharge_requested_amount
        
        %% approved amounts
        date approval_date
        double part_approved_amount
        double part_tax_approved_amount
        double part_shipping_approved_amount
        double labor_approved_amount
        double labor_tax_approved_amount
        double refrigerant_approved_amount
        double miscellaneous_approved_amount
        double fuel_surcharge_approved_amount
    }

    PROCID ||--o{ SERVICE_ORDER : ""
    MERCHANDISE ||--o{ SERVICE_ORDER : ""
    CUSTOMER ||--o{ SERVICE_ORDER : ""
    SERVICE_ORDER ||--o{ LABOR_JOB_CODE : ""
    SERVICE_ORDER ||--o{ PART : ""
    SERVICE_ORDER ||--o{ SERVICE_ATTEMPT : ""
    SERVICE_ORDER ||--o{ CLAIM : ""
