Action()
{


	lr_start_transaction("level#1");

	lr_start_sub_transaction("level#2a");

	web_url("page#1",
		"URL=http://sampledemo.neotys.com/json/cluster?s=1498867200&e=1501545599&z=3");

    lr_end_sub_transaction("level#2a");

    web_url("page#2",
    		"URL=http://sampledemo.neotys.com/json/cluster?s=1498867200&e=1501545599&z=3");

	lr_start_sub_transaction("level#2b");

    web_url("page#3",
    		"URL=http://sampledemo.neotys.com/json/cluster?s=1498867200&e=1501545599&z=3");

 	lr_end_sub_transaction("level#2b");
 	
	return 0;
}