Action(){
	int loop = 0;
    int loop_time;
    int wait_time;
    
    Login();

    lr_output_message("message");
    lr_think_time(1);
    Main();
    
    Logout();
    
    return 0;
}
