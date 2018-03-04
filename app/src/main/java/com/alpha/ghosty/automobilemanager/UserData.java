package com.alpha.ghosty.automobilemanager;

/**
 * Created by iPramodSinghRawat on 26-12-2015.
 */
public class UserData {
   	/*
	name,
	email,
	password,
	mobileNo,
	prfileType,
	currency,
	consumption_unit(Mile or Kilometer)
	pProtection
	*/
    private String _id;
    private String _name;
    private String _email;
    private String _password;
    private String _mobileNo;
    private String _prfileType;
    private String _currency;
    private String _consumptionUnit;
    private String _pProtection;

    public UserData() {
    }

    public UserData(String name, String email, String password, String mobileNo, String prfileType,
                    String currency, String consumptionUnit, String pProtection) {
        this._name = name;
        this._email = email;
        this._password = password;
        this._mobileNo = mobileNo;
        this._prfileType = prfileType;
        this._currency = currency;
        this._consumptionUnit = consumptionUnit;
        this._pProtection=pProtection;
    }
    /*
    public UserData(int id, String name, String email, String password, String mobileNo, String prfileType, String currency,
                    String consumptionUnit, String pProtection) {
        this._id = id;
        this._name = name;
        this._email = email;
        this._password = password;
        this._mobileNo = mobileNo;
        this._prfileType = prfileType;
        this._currency = currency;
        this._consumptionUnit = consumptionUnit;
        this._pProtection=pProtection;
    }
    */

    public void setId(String id) { this._id = id;    }
    public String getId() { return this._id;    }

    public void setName(String name) { this._name = name;    }
    public String getName() { return this._name;    }

    public void setEmail(String email) {
        this._email = email;
    }
    public String getEmail() {
        return this._email;
    }

    public void setPassword(String password) {
        this._password = password;
    }
    public String getPassword() {
        return this._password;
    }

    public void setPrfileType(String prfileType) {
        this._prfileType = prfileType;
    }
    public String getPrfileType() {
        return this._prfileType;
    }

    public void setMobileNo(String mobileNo) {
        this._mobileNo = mobileNo;
    }
    public String getMobileNo() {
        return this._mobileNo;
    }

    public void setCurrency(String currency) {
        this._currency = currency;
    }
    public String getCurrency() {
        return this._currency;
    }

    public void setConsumptionUnit(String consumptionUnit) {
        this._consumptionUnit = consumptionUnit;
    }
    public String getConsumptionUnit() {
        return this._consumptionUnit;
    }

    public void setpProtection(String pProtection) {
        this._pProtection = pProtection;
    }
    public String getpProtection() {
        return this._pProtection;
    }

}
