(ns for-the-glory-of-art
  (:require [quil.core :as q]))

;;The read-commands fucntion will read the turtle command file to get the commands into program.

(defn read-commands [path]
  (let [list1 (let [r (clojure.java.io/reader path)]
                   (for [line (line-seq r)]
                        (if-not (= line nil)
                        line)))]
       (into [] (for [x (map #(clojure.string/split % #"\ ") list1)]
         {:command (nth x 0) :value (nth x 1 nil)}))))

;;The commands read are assigned to turtle-commands variable. This is global variable.
(def turtle-commands (atom (read-commands "Turtle_Commands.txt")))
@turtle-commands
;;The no-of-states just keep a count of the total number of input commands.
(def no-of-states (atom (count @turtle-commands)))

;;The message variable is used to display messages on the quil window.
(def message (atom "Original Position!"))

;;The index variable points to the index of the turtle-commands vector.
;;It is incremented or decremented based on right and left arrow key pressing resp.
(def index (atom 0))

;;The current-state variableholds the current command of the turtle.
(def current-state (atom {:command nil :value nil}))

;;The state-history keeps history of executed turtle commands.
(def state-history (atom []))

;;The pen-color variable represents the current drawing color.
(def pen-color (atom 0))

;;The run-mode variable is a flag indicating whether run mode is set or not.
(def run-mode (atom false))

;;The next-command function just returns the next coomand from list of commands in turtle-commands variable.

(defn next-command []
  (str (:command (nth @turtle-commands @index)) " " (:value (nth @turtle-commands @index)))
)

;;The add-to-state-history function will add the current state and maintains a history into state-history vector.
;;This vector is used by seq-draw function to draw commands on screen.

(defn add-to-state-history [curr-state]
    (swap! state-history conj curr-state)
)

;;The undo-last-step function will pop the latest entry into state-history vector.
;;This indicates that the last step has been undone.
;;If nothing is left to be popped out, then the turtle goes back to original position.

(defn undo-last-step []
  (if-not (empty? @state-history)
    (do
      (reset! current-state (nth @state-history (- (count @state-history) 1)))
      (reset! message (str "Undo Command!    " (:command @current-state) " " (:value @current-state)))
      (swap! state-history pop)
    )
    (reset! message "Original Position!")
  )
)

;;The keyboard-action function will be called each time any key is pressed. Specific functionality is given
;;only to right arrow, left arrow and key :r.
;;With every press of right arrow the next turtle command will be executed. The command will be added to the state
;;history to be drawn on the screen.The function add-to-state-history does the adding.
;;With every left arrow key pressed, the last command gets popped from the state history. The popping indicates
;;that the last step has been undone. This give Undo functionality to the program.
;;With :r key pressed, the program goes into run mode and comes back to step mode only after all commands have been executed.
;;If any other key is pressed no action is taken.

(defn keyboard-action []
  (let [key (q/key-as-keyword)]
    (case key
      :right (if (= @index (count @turtle-commands))
               (reset! message "End of turtle commands! Now back track :)")
               (do
                 (reset! message (str "Command Executed!   " (next-command)))
                 (reset! current-state (nth @turtle-commands @index))
                 (add-to-state-history @current-state)
                 (swap! index inc)
               )
             )
      :left  (do
               (undo-last-step)
               (if-not (= @index 0)
                 (swap! index dec))
             )
      :r     (reset! run-mode true)
      :default
    )
  )
)


;;The draw-my-turtle function represnets my turtle on the quil window. My turtle is just two circles.
;;The front small circle indicates the direction.

(defn draw-my-turtle []
  (q/ellipse 0 0 25 25)
  (q/ellipse 18 0 10 10)
)

;;The setup-run-mode will see if the run mode is set to true. if yes then it add the next state into state history
;;so that seq-draw function can draw that state.

(defn setup-run-mode []
  (if @run-mode
    (if (= @index (count @turtle-commands))
      (do
        (reset! message "End of turtle commands! Now back track :)")
        (reset! run-mode false)
      )
      (do
        (reset! message (str "Run Mode!   " (next-command)))
        (reset! current-state (nth @turtle-commands @index))
        (add-to-state-history @current-state)
        (swap! index inc)
      )
    )
  )
)

;;The seq-draw function will draw sequence of commands on the screen. As the draw function starts from the beginning
;;everytime it is called hence I have maintained the state history of the draw. The seq-draw will draw all those commands
;;on the screen each time draw is called.
;;If any command is popped out of the state history then that command will not be executed in this list.
;;This way it takes care of the state of the turtle.
;;On each Move I am translating the cordinate system to the value moved. This helps place my turtle at correct position.

(defn seq-draw []
 (doseq [trans-rot @state-history]
   (let [command (:command trans-rot) value (:value trans-rot)]
      (case command
        "Move"
                (do
                  (q/line 0 0 (read-string value) 0)
                  (q/translate (read-string value) 0))
        "Turn"
                (q/rotate (q/radians (read-string value)))
        "Pen-Up"
                (do
                  (q/stroke 240)
                  (reset! pen-color 240))
        "Pen-Down"
                (do
                  (q/stroke 0)
                  (reset! pen-color 0))
        "default"
      )
   )
 )
)

;;The setup function is called just once. The framerate is set to 1. This will refresh the screen 1 time per second (1-FPS).
;;The initial background is setup to grey shade.
;;And the text size is setup to 18.

(defn setup []
  (q/smooth)
  (q/frame-rate 1)
  (q/background 240)
  (q/text-size 18))

;;The draw function is called everytime.The initial stroke and border thickness is set to 0 and 2 resp.
;;The background is set to grey each time the draw is called so that drawing dont overlap and everytime
;;the screen appears newly drawn.
;;The fill is set to black 0. This helps the text to display in black color. Then again it is set to no-fill
;;because I dont want my turtle to be a filled circle.
;;The setup-run-mode function is called to check if the run mode is set. If it is set then the next command
;;should be added to the state history vector, so that seq-draw function can draw that state.
;;Before drawing anything I translate the coordinate to the centre of the window.

(defn draw []
  (q/stroke 0)
  (q/stroke-weight 2)
  (q/background 240)
  (q/fill 255 0 0)
  (setup-run-mode)
  (q/text @message 50 20)
  (q/no-fill)
  (q/translate (/ (q/width) 2) (/ (q/height) 2))
  (seq-draw)
  (case @pen-color
    0
        (draw-my-turtle)                              ;;If the pen is down then I can directly draw the turtle
    240
        (do
          (q/stroke 0)                                ;;The turtle will always be visible hence stroke is set to 0
          (draw-my-turtle)
          (q/stroke @pen-color))                      ;;Once turtle drawing is complete, reseting the color to previous color
   )
)

;;The code below defines a new sketch named turtle. It sets the title for the window.
;;setup and draw are the name of the functions to be called.
;;size sets the size of the window.
;;keyboard-action is the name of the function that will be called once any key is pressed.

(q/defsketch turtle
  :title "My Moving Turtle"
  :setup setup
  :draw draw
  :size [700 400]
  :key-pressed keyboard-action
  :features [:keep-on-top])
