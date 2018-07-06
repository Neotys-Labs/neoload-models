Action()
{


	lr_start_transaction("level#1");

	lr_start_transaction("level#2");

	web_url("page#1",
		"URL=http://sampledemo.neotys.com/json/cluster?s=1498867200&e=1501545599&z=3");

    lr_end_transaction("level#2");

    web_url("page#2",
    		"URL=http://sampledemo.neotys.com/json/cluster?s=1498867200&e=1501545599&z=3");

    lr_end_transaction("level#1");

    web_url("page#3",
    		"URL=http://sampledemo.neotys.com/json/cluster?s=1498867200&e=1501545599&z=3");

	return 0;
}