package repository;

import model.ProgramState;
import model.statements.Statement;
import utils.FileData;
import utils.exceptions.InterpreterException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mirko on 12/10/2016.
 */
public class SingleProgramStateRepository implements Repository {
    private ProgramState programState;
    private String logFilePath;

    public SingleProgramStateRepository(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    @Override
    public void add(ProgramState programState) {
        this.programState = programState;
    }

    @Override
    public List<ProgramState> getProgramStateList() {
        List<ProgramState> list = new ArrayList<ProgramState>();
        list.add(programState);
        return list;
    }

    @Override
    public void setProgramStateList(List<ProgramState> list) throws InterpreterException {
        if (list == null || list.size() != 1)
            throw new InterpreterException("error: there should be exactly one program in the list");

        programState = list.get(0);
    }

    @Override
    public String getLogFilePath() {
        return logFilePath;
    }

    @Override
    public void logProgramState(ProgramState programState) throws InterpreterException {
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.append("Program #" + programState.getId() + "\n");
            logFile.append("Execution Stack\n");
            for (Statement statement : programState.getExecutionStack().getAll()) {
                logFile.append("  " + statement + "\n");
            }
            logFile.append("\n");

            logFile.append("Symbol Table\n");
            for (Map.Entry<String, Integer> entry : programState.getSymbolTable().getAll()) {
                logFile.append("  " + entry.getKey() + " --> " + entry.getValue() + "\n");
            }
            logFile.append("\n");

            logFile.append("Output\n");
            for (String output : programState.getOutput().getAll()) {
                logFile.append("  " + output + "\n");
            }
            logFile.append("\n");

            logFile.append("File Table\n");
            for (Map.Entry<Integer, FileData<String, BufferedReader>> entry : programState.getFileTable().getAll()) {
                logFile.append("  " + entry.getKey() + " --> " + entry.getValue() + "\n");
            }
            logFile.append("\n");

            logFile.append("Heap\n");
            for (Map.Entry<Integer, Integer> entry : programState.getHeap().getAll()) {
                logFile.append("  " + entry.getKey() + " --> " + entry.getValue() + "\n");
            }
            logFile.append("\n");

            logFile.append("-------------------\n");
        } catch (IOException error) {
            throw new InterpreterException("error: could not write to the given file");
        }
    }

    @Override
    public void serialize(String serializeFilePath) throws InterpreterException {
        try (ObjectOutputStream serializeFile = new ObjectOutputStream(new FileOutputStream(serializeFilePath))) {
            serializeFile.writeObject(this);
        } catch (IOException e) {
            throw new InterpreterException("error: could not serialize to the given file");
        }
    }

    @Override
    public void deserialize(String serializeFilePath) throws InterpreterException {
        try (ObjectInputStream serializeFile = new ObjectInputStream(new FileInputStream(serializeFilePath))) {
            SingleProgramStateRepository deserializedRepository =
                    (SingleProgramStateRepository) serializeFile.readObject();
            this.programState = deserializedRepository.programState;
            this.logFilePath = deserializedRepository.logFilePath;
        } catch (IOException e) {
            throw new InterpreterException("error: could not deserialize from the given file");
        } catch (ClassNotFoundException e) {
            throw new InterpreterException("error: could not deserialize from the given file due to class not found");
        }
    }
}
