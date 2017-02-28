# Git Help
## 一、仓库操作：
#### 1. 创建仓库 (init)
`git init`				# 在当前目录创建一个git仓库，该仓库没有远程分支

#### 2. 克隆仓库 (clone)
`git clone <url> [cloneTo]`	# 克隆一个远程仓库到本地的cloneTo目录下，cloneTo目录如果省略则已远程仓库的名称来命名本地仓库的目录

### 3. 在现有的仓库中增加远程仓库 (remote)
`git remote add <远程名称> <url>`	# 远程仓库的名称可以自己随意取，一般的名字为origin


## 二、仓库区域划分：
### 4. 工作区(.git的上级目录)
* 与.git目录平级的所有文件（除了.git目录）都是工作区中的文件
### 5. 索引区(.git/index)
* 添加到索引区的文件会被压缩后存入该文件
### 6. 本地库(.git/objects)
* 提交到本地库的文件会被压缩后放入该目录


## 三、提交代码操作：
### 7. 添加到索引区
`git add .`		# 添加当前目录下的所有改动文件到索引区（包含新建文件、删除文件和修改的文件）

`git add -u` 		# 添加仓库中的所有改动的文件到索引区（不含新建的文件）

`git add --all`	# 添加仓库中的所有能添加的文件到索引区

### 8. 提交到本地库
`git commit` 		# 将索引区的文件提交到本地库，将启动vi来提供输入提交消息，如果不输入任何内容则提交将被终止

`git commit -m` 	# -m表示后面直接跟上提交message，而不用打开一个vi编辑器来编辑提交message

`git commit -a` 	# 等同于：git add -u && git commit

`git commit --amend`	# 追加到上一个提交

### 9. 推送前拉取
* 一旦本地有提交的代码后，拉取代码的时候就应该选择`pull --rebase`

`git pull --rebase`		# 拉取最新的提交代码，再将本地的改动在最新的代码上重新改一遍

### 10. 推送代码
`git push origin master` 	# 推送本地master分支到远程仓库“origin”的master分支上（发布本地的更改）

`git push origin head`	# 推送本地的当前分支到远程仓库“origin”的同名分支上

`git push`				# 推送本地分支到远程分支，省略了远程名和分支名，需要一个设置：`git config --global push.default 'upstream'`


## 四、分支操作：
### 11. 拉取分支 (pull)
`git pull origin master` 	# 从远程仓库“origin”中拉取master分支到本地，关系：pull = fetch + merge。抓取所有分支，合并上游分支到当前分支

`git pull`				# 省略远程仓库和分支名，缺省就是origin的当前分支

`git pull --rebase` 		# fetch + rebase。抓取所有分支，变基当前分支到上游分支

### 12. 抓取分支 (fetch)
`git fetch origin`		# 从远程仓库“origin”中抓取所有分支到本地的跟踪分支

### 13. 合并分支 (merge)
`git merge origin/master`			# 将远程仓库“origin”的master分支合并到当前分支

### 14. 变基分支 (rebase)
`git rebase origin/master` 		# 将当前分支的改动以origin/master分支为基准重新改一遍

### 15. 切换分支
`git checkout [-b] dev`	# 切换到dev分支，-b表示如果dev分支不存在则创建之

### 16. 查看分支 (branch)
`git branch [-a]`			# 查看本地的分支，-a表示列出本地分支和远程分支

### 17. 删除分支
`git branch -d dev`		# -d表示删除dev分支(如果该分支有提交没有被合并则拒绝删除)，-D表示强制删除

### 18. 重置分支
`git reset head`			# 重置本地库和索引区到head，但是不重置工作区

`git reset --hard master`	# 重置当前head指针到master，包括工作区、索引区和本地库（最严格的重置）

`git reset head^`			# ^表示当前head的父提交

`git reset master^^`		# ^^表示master的前面两个提交

`git reset head~1`		# ~n表示head前n个提交

### 分支说明：
* 本地分支与上游(跟踪)分支：本地分支可以提交；上游/远程/跟踪分支不能提交，只能通过push和pull的方式改动
* master分支：git的缺省分支

## 五、其他操作
### 19. 其他常用命令
`git status`			# 查看工作区状态。常用选项：-s -u

`git diff`			# 查看工作区与索引区/本地库中的差异

`git log`				# 查看提交日志（历史）。--oneline -<number>

`git cherry-pick`		# 挑选提交到当前分支

`git revert`			# 将某个条取反后提交到当前分支。取反：原本是增加一行就删除改行，原本是删除一行就增加改行

## 参数说明：
* git命令的参数中，一般单字母的参数前面只有一个-(如：`-a`)，非单字母的参数前面有两个-(如：`--oneline`)。如：git add -u, git add --update
* "-- "后面一般可以跟文件名（支持通配符）。如：`git log -- *.sh`

## ssh
* git支持多种通信协议：http, https, git, ssh。
* 推荐使用ssh协议，这样可以免去每次提交代码需要输入密码的问题（当然不用ssh也可以设置保存密码）。需先在个人设置页面上传 SSH-RSA 公钥，完成配对验证。

#### 设置记住密码：
`git config --global credential.helper "store"`

#### 设置用户和邮箱
`git config --global user.name "xxx"`				# name可以是中文

`git config --global user.email "xxx@xxx.xxx"`
