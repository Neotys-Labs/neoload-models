Action()
{


	lr_start_transaction("My transaction");

	web_url("page4",
		"URL=http://sampledemo.neotys.com/json/cluster?s=1498867200&e=1501545599&z=3");


    lr_end_transaction("My transaction");

	return 0;
}