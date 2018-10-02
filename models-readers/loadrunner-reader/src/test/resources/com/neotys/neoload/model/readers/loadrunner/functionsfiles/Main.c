MainTransaction()
{
	
	// Enter Transaction
	sapgui_select_active_window("wnd[0]");
	sapgui_set_ok_code("/ntest");
	
	lr_think_time(1);

    // Cross Reference: not supported by NeoLoad
    Action();

	return 0;
}
