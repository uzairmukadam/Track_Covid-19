# TRACK COVID-19

[![author](https://img.shields.io/badge/Author-Mukadam%20Uzair-green.svg)](http://theuzitech.com/)

**About**

This is my first public android project to check Covid-19 reports of your country and other nations.
The application auto detects the location and returns with you country data on top. You can browse through other nation datas and come back to your nation with the click of a button.
This is a growing project with local data of different nations to be soon added. You can help me out by requesting an issue with the api details that provide with local data.

**API**

https://github.com/NovelCOVID/API	(For Global numbers and US local data)
https://api.covid19india.org	(For India local data)

##Adding Local Data Support for your Nation

###Changes in DataHelper.class
----

1)In getURL() function add data in the format:

    if(country.toLowerCase().equals(YOUR_COUNTRY_IN_LOWERCASE)){
    		return API LINK FOR THE DATA;
    }
    
2)For formating the data to be compatible with the view add in formatData()

    if(country.toLowerCase().equals(YOUR_COUNTRY_IN_LOWERCASE)){
    		return YOUR_COUNTRY_IN_LOWERCASEObject(response);
    }

c)Create JSONArray YOUR_COUNTRY_IN_LOWERCASEObject(response) function in DataHelper. This function is used to create a new JSONArray according to the required data format.

	i)Array should contain objects of different states/regions/cities of your country.
    ii)First element of the object has to be state/region/city name.
    iii)Later elements need to be placed as per you wish to display.
