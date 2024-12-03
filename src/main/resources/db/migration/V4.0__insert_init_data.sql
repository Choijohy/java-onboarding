INSERT INTO `user` VALUES
                       (1,'user1@gmail.com','1234'),
                       (2,'user2@gmail.com','1234'),
                       (3,'user3@gmail.com','1234');

INSERT INTO `tag` VALUES
    (3,'C++ STUDY');


INSERT INTO `board` VALUES
                        (3,1,'title1','contnent1'),
                        (4,1,'title2','파이썬에 대하여'),
                        (5,2,'프로그래밍','절차지향 프로그래밍과 객체 지향 프로그래밍');

INSERT INTO `board_tag` VALUES
                            (3,1),
                            (5,1),
                            (3,2),
                            (4,2),
                            (5,3);

INSERT INTO `board_like` VALUES
                             (5,1),
                             (3,2),
                             (5,2);
