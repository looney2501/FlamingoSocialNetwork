package com.map_toysocialnetwork_gui.UI;

public abstract class AbstractUserInterface {

    protected boolean finished;
    /**
     * Runs the user interface.
     */
    public void run() {
        finished = false;
        while(!finished) {
            showMenu();
            int inputCommand = readCommand();
            if (inputCommand == 0) {
                finished = true;
            }
            else {
                executeCommand(inputCommand);
            }
        }
    }

    /**
     * Prints all the menu options.
     */
    protected abstract void showMenu();

    /**
     * Reads the input command of the user.
     * @return integer representing the command of the user.
     */
    protected abstract int readCommand();

    /**
     * Executes the command selected by the user.
     * @param inputCommand integer representing the command of the user.
     */
    protected abstract void executeCommand(int inputCommand);
}
