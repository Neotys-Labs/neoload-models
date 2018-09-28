Logout()
{
	lr_think_time(1);
	
	if (sapgui_is_object_available("wnd[0]"))
	{
		sapgui_select_active_window("wnd[0]");
		
		if (sapgui_is_object_available("tbar[0]/okcd"))
		{
			sapgui_set_ok_code("/nEX");
			sapgui_send_vkey(ENTER);
		};
	}
	
	lr_think_time(1);
	
	return 0;
}