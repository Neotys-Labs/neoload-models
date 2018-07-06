vuser_init()
{
    web_submit_data("login",
        "Action=http://sampledemo.neotys.com/login",
        "Method=POST",
        "EncType=multipart/form-data",
        "RecContentType=text/html",
        "Referer=http://sampledemo.neotys.com/reports/submit",
        "Snapshot=t22.inf",
        "Mode=HTML",
        ITEMDATA,
        "Name=login", "Value={login}", ENDITEM,
        "Name=password", "Value={password}", ENDITEM,
        "Name=submit", "Value=Submit", ENDITEM,
        EXTRARES,
        "URL=../scheduler", "Referer=http://sampledemo.neotys.com/reports/thanks", ENDITEM,
        LAST);
	return 0;
}
