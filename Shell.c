/*
 * File:    Shell.c
 * Author:  Eric Humber
 * Date:    2020/05/30
 * Version: 2.0
 * 
 * Purpose: This program is a personalized implementation of a rudimentary
 *          shell. It allows the user to enter single or multiple commands
 *          with or without arguments, as well as with the option of starting
 *          a process in the background. This shell also allows the user to
 *          pipe the output of a single command into the input of one other
 *          command. It also prompts the user to enter something when the
 *          input is empty, and allows them to exit upon typing "exit".
 * 
 * Notes:   While some of the command formatting algorithms may look
 *          repetitive, they were intentionally left that way to aid in
 *          readability and the possibility of making quick and precise
 *          modular changes.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

#define MAX_CHARS 80
#define MAX_ARGS 40

/*
 * Name:        execute
 * Purpose:     To fork() and execvp() a command or series thereof, depending
 *              upon the user's input.
 * Arguments:   An array containing commands/arguments to be executed, as well
 *              as a boolean indicating whether a command is to be placed in
 *              the background.
 * Output:      The result of calling the given commands/arguments and their
 *              corresponding pid.
 * Modifies:    stdout
 * Returns:     Output to stdout and a pid.
 * Assumptions: That the user follows the input guidelines laid out in the
 *              Assignment #2 instructions.
 * Bugs:        Does not handle inconvenient placement of subsequent output
 *              from background processes that have been reaped, as noted
 *              in the assignment instructions.
 * Notes:       None.
 */
int
execute(char **cmd_name, bool is_bg_job)
{
    int status;
    pid_t execute_pid;

    execute_pid = fork();

    if (execute_pid < 0)
    {
        // Fork error check
        printf("fork() failed\n");
        exit(-1);
    }
    else if (execute_pid == 0)
    {
        // Child process
        int run = execvp(cmd_name[0], cmd_name);
        exit(1);
        printf("execvp() failed\n");
    }
    else
    {
        if (is_bg_job == false)
        {
            waitpid(execute_pid, &status, 0);
        }
        else
        {
            // If is_bg_job, parent process does not wait on child process
        }
    }

    return (int) execute_pid;
}

/*
 * Name:        pipe_line
 * Purpose:     To fork() and execvp() a given command with/without arguments,
 *              and pipe it into another such command.
 * Arguments:   An array containing the commands/arguments to be piped in,
 *              and an array containing commands/arguments for it to be
 *              piped through.
 * Output:      The results of piping the pipe_args_in array into the
 *              pipe_args_out array.
 * Modifies:    stdout
 * Returns:     Output to stdout and a pid.
 * Assumptions: That the user follows the input guidelines laid out in the
 *              Assignment #2 instructions.
 * Bugs:        None.
 * Notes:       As noted in the assignment instructions, this function can
 *              only handle a singular pipe.
 */
int
pipe_line(char **pipe_args_in, char **pipe_args_out)
{
    int status;
    int stdin = 0;
    int stdout = 1;
    int pipe_read_end = 0;
    int pipe_write_end = 1;

    int fd[2];
    pid_t pipe_pid;
    pipe(fd);

    // Pipe error check
    if (pipe(fd) == -1)
    {
        printf("Pipe failed\n");
        return 1;
    }

    // Fork error check
    pipe_pid = fork();
    if (pipe_pid < 0)
    {
        printf("fork() failed\n");
        return 1;
    }

    // Deal with pipe_args_in
    if (pipe_pid == 0)
    {
        // Grab results from pipe_args_in and pipe to pipe_args_out
        dup2(fd[stdout], pipe_write_end);
        execvp(pipe_args_in[0], pipe_args_in);

        // Close used pipes
        // close(fd[pipe_write_end]);
        // close(fd[stdout]);
    }
    
    // Second fork to deal with pipe_args_out
    if (fork() == 0)
    {
        // Fork and execvp pipe_args_out
        dup2(fd[stdin], pipe_read_end);
        execvp(pipe_args_out[0], pipe_args_out);

        // Close used pipes
        // close(fd[pipe_read_end]);
        // close(fd[stdin]);
    }
    
    // Ensure all pipe ends are closed
    close(fd[pipe_write_end]);
    close(fd[stdout]);
    close(fd[pipe_read_end]);
    close(fd[stdin]);

    waitpid(pipe_pid, &status, 0);
    return (int) pipe_pid;
}

/* Handles input, formatting, and calling execute/pipe_line functions */
int
main()
{
    char *str = (char *)calloc(MAX_CHARS, sizeof(char));
    char *tok;
    char *cmd_name[MAX_ARGS + 1];

    int i = 0; // Number of commands and/or arguments in str
    int j = 0; // Number of semicolons in str
    int k = 0; // Number of background jobs requested

    bool is_bg_job = false;
    bool is_pipe_request = false;

    char *pipe_args_in[(MAX_ARGS / 2) + 1];
    char *pipe_args_out[(MAX_ARGS / 2) + 1];

    // Print initial prompt
    printf("$ ");
    fflush(stdout);

    // Get initial line
    fgets(str, MAX_CHARS - 1, stdin);

    // Loop for user input lines until input is "exit"
    while (strcmp(str, "exit\n") != 0)
    {

        // Make arrays happy
        memset(cmd_name, '\0', MAX_ARGS + 1);
        memset(pipe_args_in, '\0', (MAX_ARGS / 2) + 1);
        memset(pipe_args_out, '\0', (MAX_ARGS / 2) + 1);

        // Parse out the tokens ("words") on that line
        tok = strtok(str, " \t");

        // If no commands on the current line
        if (strcmp(tok, "\n") == 0)
        {
            // Empty line, nothing to process here, go read another line
            printf("Enter a line of valid command(s) or type \"exit\". "
                    "If you see no output,\nyour input was invalid or "
                    "there were no results to return.\n");
        }

        // Reset counters
        i = 0;
        j = 0;
        k = 0;

        // Copy tok to cmd_name and increment for next tok
        cmd_name[i] = tok;
        i++;

        // Parse a single line
        while ((tok = strtok(NULL, " \t")) != NULL)
        {
            // Handle single command/arg
            cmd_name[i] = tok;

            // Get second part(s) of pipe if applicable
            if (is_pipe_request == true)
            {
                pipe_args_out[i] = tok;
            }

            // Increment for next index in respective arg array
            i++;

            // Handle background process request
            if (strcmp(tok, "&") == 0)
            {
                is_bg_job = true;
                k++;

                // Remove \n chars to handle commands without and with args
                if (i == 1)
                {
                    str[strlen(str)-1] = '\0';
                }
                else if (i > 1)
                {
                    cmd_name[i-1][strlen(cmd_name[i-1])-1] = '\0';
                }
                cmd_name[i-1] = NULL; // Remove & symbol from cmd_name

                // Pass command to execute function
                pid_t execute_pid;
                int execute_return_value = execute(cmd_name, is_bg_job);
                execute_pid = execute_return_value;

                // Prep array for next command and reset is_bg_job bool
                memset(cmd_name, '\0', MAX_ARGS + 1);
                i = 0;
                is_bg_job = false;
            }

            // Handle additional commands
            else if (strcmp(tok, ";") == 0)
            {
                j++;

                // Remove \n chars in handling commands without and with args
                if (i == 1)
                {
                    str[strlen(str)-1] = '\0';
                }
                else if (i > 1)
                {
                    cmd_name[i-1][strlen(cmd_name[i-1])-1] = '\0';
                }
                cmd_name[i-1] = NULL; // Remove ; symbol from cmd_name

                // Pass command to execute function
                pid_t execute_pid;
                int execute_return_value = execute(cmd_name, is_bg_job);
                execute_pid = execute_return_value;

                // Prep array for next command
                memset(cmd_name, '\0', MAX_ARGS + 1);
                i = 0;
            }

            // Handle pipe request
            else if (strcmp(tok, "|") == 0)
            {
                is_pipe_request = true;

                // Remove \n chars in handling commands without and with args
                if (i == 1)
                {
                    str[strlen(str)-1] = '\0';
                }
                else if (i > 1)
                {
                    cmd_name[i-1][strlen(cmd_name[i-1])-1] = '\0';
                }
                cmd_name[i-1] = NULL; // Remove | symbol from cmd_name

                // Pass previous storage to pipe_args_in/remove from cmd_name
                for (int l = 0; l < i; l++)
                {
                    pipe_args_in[l] = cmd_name[l];
                    cmd_name[l] = NULL;
                }

                i = 0;
            }
        }

        // Remove \n chars in case of single-command input
        if (i == 1 && j == 0 && k == 0) // Single command, no args
        {
            str[strlen(str)-1] = '\0';
            cmd_name[i] = NULL;
        }
        else if (i > 1 && j == 0 && k == 0) // Single command, with args
        {
            cmd_name[i-1][strlen(cmd_name[i-1])-1] = '\0';
            cmd_name[i] = NULL;
        }

        // Format final command in case of multi-command/bg process input
        if (j >= 1 || k >= 1)
        {
            cmd_name[i-1][strlen(cmd_name[i-1])-1] = '\0';
            cmd_name[i] = NULL;
        }

        // Formatting and bool setting for bg processes
        if (strcmp(cmd_name[i-1], "&") == 0)
        {
            cmd_name[i-1] = NULL;
            is_bg_job = true;
        }

        // Deal with formatting in case of pipe request
        if (is_pipe_request == true)
        {
            pipe_args_out[i] = NULL;
        }

        // If pipe request, pass command to pipe function
        if (is_pipe_request == true)
        {
            pid_t pipe_pid;
            int pipe_return_value = pipe_line(pipe_args_in, pipe_args_out);
            pipe_pid = pipe_return_value;
        }

        // If not pipe request, pass command to execute function
        else
        {
            pid_t execute_pid;
            int execute_return_value = execute(cmd_name, is_bg_job);
            execute_pid = execute_return_value;
        }

        // Reap background jobs
        int status;
        int reap_pid = waitpid(-1, &status, WNOHANG);
        if (reap_pid > 0)
        {
            printf("Process %d terminated\n", (int) reap_pid);
        }

        // Reset bool values
        is_bg_job = false;
        is_pipe_request = false;

        // Next prompt
        printf("\b$ ");
        fflush(stdout);

        // Get next line
        str = realloc(str, MAX_CHARS * sizeof(char));
        fgets(str, MAX_CHARS - 1, stdin);
    }

    free(str);
    return 0;
}
