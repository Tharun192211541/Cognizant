\# ☁️ AWS NETWORKING: THE COMPLETE MASTER GUIDE



\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_



\### 1. VPC (Virtual Private Cloud)

\*\*The Concept:\*\* Think of a VPC as your own private plot of land in the cloud. It is a logically isolated section of the AWS cloud where you launch your resources.



\* \*\*IPv4 CIDR Range:\*\* This defines your IP address "pool."

\* \*\*Example:\*\* `10.0.0.0/24`

\* \*\*The Math:\*\* In a `/24` range, the first three octets (`10.0.0`) are fixed. Only the last octet changes, giving you a range from \*\*10.0.0.1 to 10.0.0.254\*\*.



\*\*How to create:\*\*

> VPC Dashboard ➔ Your VPCs ➔ Create VPC ➔ Provide Name and CIDR IPv4 ➔ Create.



\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_



\### 2. SUBNETS (The Internal Rooms)

\*\*The Concept:\*\* Subnets are a defined set of IP addresses used to increase security and efficiency. You have built your compound walls (VPC) and now you are building the individual buildings.



\* \*\*Public Subnet (`10.0.1.0/24`):\*\* The "Front Porch." Used for web servers that face the internet.

\* \*\*Private Subnet (`10.0.2.0/24`):\*\* The "Living Room." Used for application logic and internal servers.

\* \*\*Database Subnet (`10.0.3.0/24`):\*\* The "Locked Vault." For sensitive data storage.



\*\*How to create:\*\*

> Subnets ➔ Create Subnet ➔ Select VPC ➔ Provide Name and Subnet CIDR Range ➔ Create.



\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_



\### 3. GATEWAYS (The Entry \& Exit)

\*\*The Concept:\*\* Without gateways, your VPC is isolated. You need doors to let traffic communicate with other networks.



\#### \*\*Internet Gateway (IGW)\*\*

\* \*\*Purpose:\*\* The bridge that allows your VPC to communicate with the Internet.

\* \*\*Crucial Step:\*\* By default, it is created "Detached." You must manually \*\*Attach to VPC\*\* to enable it.



\#### \*\*NAT Gateway\*\*

\* \*\*Purpose:\*\* Allows instances in the \*\*Private Subnet\*\* to "talk out" to the internet (for updates) but prevents the internet from initiating a connection to them.

\* \*\*Placement:\*\* Always place the NAT Gateway in the \*\*Public Subnet\*\*.



\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_



\### 4. ROUTE TABLES (The Traffic GPS)

\*\*The Concept:\*\* Even with a Gateway, subnets don't know where to go without a map. The Route Table is the bridge between the subnet and the outside world.



\* \*\*Public Route Table:\*\* \* \*\*Route:\*\* Destination `0.0.0.0/0` (Whole Internet) ➔ \*\*Target:\*\* Internet Gateway (igw-xxxx).

&#x20;   \* \*\*Association:\*\* Attach this to your Public Subnet.



\* \*\*Private Route Table:\*\* \* \*\*Route:\*\* Destination `0.0.0.0/0` ➔ \*\*Target:\*\* NAT Gateway (nat-xxxx).

&#x20;   \* \*\*Association:\*\* Attach this to your Private Subnet.



\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_



\### 5. SECURITY GROUPS (The Digital Bouncers)

\*\*The Concept:\*\* These are your instance-level firewalls. This is where the most important security "Pro-Tips" happen.



\#### \*\*Public Subnet (Bastion/Web Host)\*\*

\* \*\*Inbound Rule:\*\* Change SSH (Port 22) source from `0.0.0.0/0` to \*\*My IP\*\*.

\* \*\*Result:\*\* Only your specific laptop can even try to knock on the door. To everyone else, the server doesn't even exist.



\#### \*\*Private Subnet (Database/Internal App)\*\*

\* \*\*Inbound Rule:\*\* Change the SSH source to the \*\*Security Group ID of the Public Instance\*\*.

\* \*\*Result:\*\* This is the "Magic" of AWS. The Private Instance will only accept requests if they come from a machine wearing the "Public Instance" badge.



\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_



\### 💡 FINAL PRO-TIPS

1\.  \*\*Cost Management:\*\* NAT Gateways cost money per hour. Delete them and release the \*\*Elastic IP\*\* when testing is finished!

2\.  \*\*Access Chain:\*\* You never go direct to private. The flow is: \*\*Laptop\*\* ➔ \*\*Public Instance\*\* ➔ \*\*Private Instance\*\*.

3\.  \*\*Permissions:\*\* Ensure your `.pem` key has the correct permissions (`chmod 400`) or the connection will be rejected.



\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_

