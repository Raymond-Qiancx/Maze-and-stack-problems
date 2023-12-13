package Linear_Structure;

import java.util.ArrayList;
import java.util.List;

/* 定义一个链表*/
class StackNode<T> {
    T data;
    StackNode<T> next;

    public StackNode(T data) {
        this.data = data;
    }
}

/* 定义一个链表型的栈*/
class LinkedStack<T> {
    private StackNode<T> top; //定义头节点

    public LinkedStack() { //构造方法
        this.top = null;
    }

    public boolean isEmpty() {
        return top == null;
    } //功能1:检验栈是否为空

    public void push(T data) {
        StackNode<T> newNode = new StackNode<>(data);
        newNode.next = top;
        top = newNode;
    } //功能2:往栈内插入新节点

    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty!");
        }
        T data = top.data;
        top = top.next;
        return data;
    } //功能3:弹出新节点

    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty!");
        }
        return top.data;
    } //功能4:查看栈顶元素大小
}

// 表示迷宫中的位置及其方向的类
class Position {
    int x,y,dir;

    public Position(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }
}

// 表示8个可能的移动方向的枚举
enum Direction {
    // 由于输出需为三元组，其中dir表示移动方向
    RIGHT(0), DOWN(1), LEFT(2), UP(3);

    private final int dirCode;

    Direction(int dirCode) {
        this.dirCode = dirCode;
    }

    public int getDirCode() {
        return dirCode;
    }
}

public class mazeSolver {
    // 四个方向移动的坐标变化量，顺序依次为右、下、左、上
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    // 判断坐标 (x, y) 是否在迷宫内
    private static boolean inBounds(int x, int y, int[][] maze) {
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length;
    }

    // 非递归求解迷宫路径
    public static void findPath(int[][] maze, int startX, int startY, int endX, int endY) {
        LinkedStack<Position> stack = new LinkedStack<>();
        // 将起始位置信息压入栈中，方向选择为向右方向
        stack.push(new Position(startX, startY, Direction.RIGHT.getDirCode()));

        while (!stack.isEmpty()) {
            // 不断从栈中弹出当前位置的信息，直到栈为空或者找到了终点位置
            Position current = stack.pop();
            
            // 如果达到终点，打印路径并返回
            if (current.x == endX && current.y == endY) {
                printPath(stack);
                return;
            }

            // 尝试所有方向：0：right, 1: down, 2: left, 3: up
            for (int d = current.dir; d < 4; d++) {
                int nextX = current.x + MOVES[d][0];
                int nextY = current.y + MOVES[d][1];

                // 如果下一步可达，并且没有被访问过，则入栈
                if (inBounds(nextX, nextY, maze) && maze[nextX][nextY] == 0) {
                    maze[current.x][current.y] = -1; // 标记当前位置已访问
                    // 记录当前位置，以便后序返回该位置尝试下一方向--回溯思想
                    stack.push(new Position(current.x, current.y, d+1 ));
                    // 尝试下一个位置，并从右侧开始探索
                    stack.push(new Position(nextX, nextY, Direction.RIGHT.getDirCode()));
                    break; //移动到下一个位置，不再尝试其他方向
                }

            }
        }
        System.out.println("No path found.");
    }

    // 打印从起点到当前位置的路径
    private static void printPath(LinkedStack<Position> stack) {
        //定义新栈--存储原栈中元素的反向顺序
        LinkedStack<Position> reverseStack = new LinkedStack<>();
        while (!stack.isEmpty()) {
            reverseStack.push(stack.pop());
            //将原栈中的元素以此弹出并存入新栈
        }
        while (!reverseStack.isEmpty()) {
            // 将传入栈的 Position 对象按照相反的顺序打印出来
            Position pos = reverseStack.pop();
            System.out.println("(" + pos.x + ", " + pos.y + ", " + pos.dir + ")");
        }
    }

    public static void main(String[] args) {
        int[][] maze = {
                { 1, 0, 1, 1, 1, 1, 1, 1, 1, 1 },
                { 1, 0, 0, 1, 0, 0, 0, 1, 0, 1 },
                { 1, 0, 0, 1, 0, 0, 0, 1, 0, 1 },
                { 1, 0, 0, 0, 0, 1, 1, 0, 0, 1 },
                { 1, 0, 1, 1, 1, 0, 0, 0, 0, 1 },
                { 1, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
                { 1, 0, 1, 0, 0, 0, 1, 0, 0, 1 },
                { 1, 0, 1, 1, 1, 0, 1, 1, 0, 1 },
                { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
        };

        // 设定起点和终点
        int startX = 0, startY = 1;
        int endX = 8, endY = 9;

        // 开始求解
        findPath(maze, startX, startY, endX, endY);
    }
}

/* 输出结果：
(0, 1, 2) (1, 1, 1) (1, 2, 2) (2, 2, 2) (3, 2, 3) (3, 1, 2) (4, 1, 2)
(5, 1, 1) (5, 2, 1) (5, 3, 2) (6, 3, 1) (6, 4, 1) (6, 5, 2) (7, 5, 2)
(8, 5, 1) (8, 6, 1) (8, 7, 1) (8, 8, 1)
 */

class MazeSolverRecursive {
    // 四个方向移动的坐标变化量，顺序依次为右、下、左、上
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    // 存储迷宫路径的可变列表
    public static List<Position> path = new ArrayList<>();

    // 判断坐标 (x, y) 是否在迷宫内
    private static boolean inBounds(int x, int y, int[][] maze) {
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length;
    }

    // 递归方法来找到从 (x, y) 到达 (endX, endY) 的所有路径
    public static void findPathsRecursive(int[][] maze, int x, int y,
                                                  int endX, int endY) {
        // 如果 (x, y) 是通路
        if (x == endX && y == endY) {
            path.add(new Position(x, y, -1)); // 将终点添加至路径
            printPath(); // 打印路径
            path.remove(path.size() - 1); // 回溯：将该路径删掉
            return; //在探索其它路径时，不会受到已找到路径的干扰
        }

        // 如果 (x, y) 超出边界或是障碍
        if (!inBounds(x, y, maze) || maze[x][y] != 0) {
            return;
        }

        maze[x][y] = -1; // 标记该位置已访问
        path.add(new Position(x, y, -1)); // 将该位置添加至路径

        // 尝试所有方向移动
        for (int i = 0; i < MOVES.length; i++) {
            int nextX = x + MOVES[i][0];
            int nextY = y + MOVES[i][1];
            findPathsRecursive(maze, nextX, nextY, endX, endY);
        } // 递归尝试下一个位置

        // 回溯：将当前位置从 path 中移除，并将 maze 中的该位置状态重置为 0，
        // 表示这个位置再次可用于后续路径的探索
        path.remove(path.size() - 1);
        maze[x][y] = 0;
    }

    // 打印路径
    private static void printPath() {
        times++;
        for (Position pos : path) {
            System.out.printf("(%d, %d)\n", pos.x, pos.y);
        }
        System.out.println();
    }

    private static int times=0; // 有效迭代次数

    public static void main(String[] args) {
        int[][] maze = {
                { 1, 0, 1, 1, 1, 1, 1, 1, 1, 1 },
                { 1, 0, 0, 1, 0, 0, 0, 1, 0, 1 },
                { 1, 0, 0, 1, 0, 0, 0, 1, 0, 1 },
                { 1, 0, 0, 0, 0, 1, 1, 0, 0, 1 },
                { 1, 0, 1, 1, 1, 0, 0, 0, 0, 1 },
                { 1, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
                { 1, 0, 1, 0, 0, 0, 1, 0, 0, 1 },
                { 1, 0, 1, 1, 1, 0, 1, 1, 0, 1 },
                { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
        };

        // 设定起点和终点
        int startX = 0, startY = 1;
        int endX = 8, endY = 9;

        // 开始递归寻找所有路径
        findPathsRecursive(maze, startX, startY, endX, endY);

        // 如果没有任何路径被找到，打印消息
        if (path.isEmpty()) {
            System.out.println("No path found.");
        }

        System.out.println(times);//有效迭代次数
    }
}

/*
(0, 1) (1, 1) (2, 1) (3, 1)
(4, 1) (5, 1) (5, 2) (5, 3)
(6, 3) (6, 4) (6, 5) (5, 5)
(4, 5) (4, 6) (5, 6) (5, 7)
(4, 7) (3, 7) (3, 8) (4, 8)
(5, 8) (6, 8) (7, 8) (8, 8)
(8, 9)
*/
