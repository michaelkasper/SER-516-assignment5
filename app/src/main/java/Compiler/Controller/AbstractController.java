package Compiler.Controller;

import javax.swing.*;

/**
 * The AbstractController just normalizes the different
 * controllers so they all use the same router
 * structure and must define a getView method
 */
public abstract class AbstractController {
    public abstract JPanel getView();
}
