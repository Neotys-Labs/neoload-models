Action()
{

	web_add_cookie();

	myMetthod("test"+test,toto);

	myMetthod(test , anotherTest);

	web_url("page1",
		"URL=http://sampledemo.neotys.com/json/cluster?s=1498867200&e=1501545599&z=3",
		"Resource=0",
		"RecContentType=text/html",
		"Referer=http://sampledemo.neotys.com/",
		"Snapshot=t18.inf",
		"Mode=HTML",
		EXTRARES,
		"Url=../favicon.ico", "Referer=", ENDITEM,
		"Url=../myimage.jpg", "Referer=", ENDITEM,
		LAST);

	unknown_method(test);

	lr_think_time(1);
	
	web_reg_save_param_regexp("ParamName=UserDetails",
			"RegExp=aaa",
			SEARCH_FILTERS,
			"Scope=Body",
			"RequestUrl=*/onboarding/viewTaskTypes*",
			"GROUP=1",
			LAST);					

	web_url("page2",
    		"URL=http://sampledemo.neotys.com/json/page2?s=1498867200&e=1501545599&z=3",
    		"Resource=0",
    		"RecContentType=text/html",
    		"Referer=http://sampledemo.neotys.com/",
    		"Snapshot=t18.inf",
    		"Mode=HTML",
    		EXTRARES,
    		LAST);
    		
    web_cache_cleanup();	
    
    web_cleanup_cookies();	

	return 0;
}