Action()
{

	if (sapgui_is_object_available("wnd[1]")){
		sapgui_set_ok_code("1");		
		sapgui_set_ok_code("2");		
	} else {
		sapgui_set_ok_code("3");
		if (true){
			sapgui_set_ok_code("4");
		}
		if (false){
			sapgui_set_ok_code("4");
		}		
	}	
	return 0;
}