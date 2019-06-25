Action()
{
    if(true)
	{
		lr_start_transaction("myTransaction");
		lr_think_time(1);
		lr_end_transaction("myTransaction");
	}
	return 0;
}