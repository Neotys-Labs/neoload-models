Login()
{
	Logout();
	lr_start_transaction("start");
	sapgui_open_connection_ex("", lr_eval_string(""), "con[0]");
	lr_end_transaction("start");

	sapgui_select_active_connection("con[0]");
	sapgui_select_active_session("ses[0]");
	sapgui_select_active_window("wnd[0]");

	lr_start_transaction(lr_eval_string("_{sid}_signin"));
	sapgui_logon("{username}",lr_decrypt(lr_eval_string("{password}")), "{client}", "EN");
	lr_end_transaction(lr_eval_string("_{sid}_signin"), LR_AUTO);

	lr_think_time(1);
	
	return 0;
}
