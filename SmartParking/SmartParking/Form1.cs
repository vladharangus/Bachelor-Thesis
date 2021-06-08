using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Diagnostics;

namespace SmartParking
{
    public partial class Form1 : Form
    {
        SqlConnection connection;
        int start;
        int finish;
        Stopwatch stopwatch = new Stopwatch();

        public Form1()
        {
            connection = new SqlConnection(@"Data Source = DESKTOP-1NBNSDK\SQLEXPRESS; Initial Catalog = SmartParking; Integrated Security = true");
            connection.Open();
            this.start = 1;
            this.finish = 10;
            InitializeComponent();
          
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            string line;
            string insert = "insert into Car values (@x, @y, @gender, @ise)";
            SqlCommand cmdInsert = new SqlCommand(insert, connection);
            cmdInsert.Parameters.Add("@x", SqlDbType.Decimal);
            cmdInsert.Parameters.Add("@y", SqlDbType.Decimal);
            cmdInsert.Parameters.Add("@gender", SqlDbType.VarChar);
            cmdInsert.Parameters.Add("@ise", SqlDbType.VarChar);

            int index = this.start;
           
            System.IO.StreamReader file =
                new System.IO.StreamReader(@"C:\Users\Vlad\source\repos\SmartParking\SmartParking\Cars.txt");
            while ((line = file.ReadLine()) != null && index <= this.finish)
            {
                string[] attributes = line.Split(' ');
                decimal X = Convert.ToDecimal(attributes[1]);
                decimal Y = Convert.ToDecimal(attributes[2]);
                String gender = attributes[3];
                String ise = attributes[4];
                cmdInsert.Parameters["@x"].Value = X;
                cmdInsert.Parameters["@y"].Value = Y;
                cmdInsert.Parameters["@gender"].Value = gender;
                cmdInsert.Parameters["@ise"].Value = ise;
                cmdInsert.ExecuteNonQuery();
                index++;
            }

            PopulateGrids();
            this.start =index;
            if (this.finish == 1000)
            {
                stopwatch.Stop();
                string insertTime = "insert into [Execution Time] values (@time)";
                SqlCommand cmdInsertTime = new SqlCommand(insertTime, connection);
                cmdInsertTime.Parameters.Add("@time", SqlDbType.Int);
                cmdInsertTime.Parameters["@time"].Value = stopwatch.ElapsedMilliseconds;
                cmdInsertTime.ExecuteNonQuery();
                System.Windows.Forms.Application.Exit();
            }
                

            this.finish = this.start + 9;
          
           
        }

        private void Form1_Load(object sender, EventArgs e)
        {
           
            LoadParkingLots();
            PopulateGrids();
            timerCars.Start();
            timerRequests.Start();
            timerDelete.Start();
            stopwatch.Start();
        }

        private void LoadParkingLots()
        {
            string line;
            string insert = "insert into ParkingLot values (@x, @y, @spots, @free)";
            SqlCommand cmdInsert = new SqlCommand(insert, connection);
            cmdInsert.Parameters.Add("@x", SqlDbType.Decimal);
            cmdInsert.Parameters.Add("@y", SqlDbType.Decimal);
            cmdInsert.Parameters.Add("@spots", SqlDbType.Int);
            cmdInsert.Parameters.Add("@free", SqlDbType.Int);

            // Read the file and display it line by line.  
            System.IO.StreamReader file =
                new System.IO.StreamReader(@"C:\Users\Vlad\source\repos\SmartParking\SmartParking\ParkingLots.txt");
            while ((line = file.ReadLine()) != null)
            {
                string[] attributes = line.Split(' ');
                decimal X = Convert.ToDecimal(attributes[0]);
                decimal Y = Convert.ToDecimal(attributes[1]);
                int spots = Convert.ToInt32(attributes[2]);
                int free = Convert.ToInt32(attributes[3]);
                cmdInsert.Parameters["@x"].Value = X;
                cmdInsert.Parameters["@y"].Value = Y;
                cmdInsert.Parameters["@spots"].Value = spots;
                cmdInsert.Parameters["@free"].Value = free;
                cmdInsert.ExecuteNonQuery();
            }
        }
        private void PopulateGrids()
        { 

            DataSet lotsData = new DataSet();
            DataSet carsData = new DataSet();
            DataSet recordsData = new DataSet();

            SqlDataAdapter dataAdapterLots = new SqlDataAdapter("select PID, Spots, FreeSpots from ParkingLot", connection);
            SqlDataAdapter dataAdapterCars = new SqlDataAdapter("select CID, Gender, isElectrical from Car", connection);
            SqlDataAdapter dataAdapterRecords = new SqlDataAdapter("select CarStatus, CID, PID from CarRecords", connection);

            dataAdapterLots.Fill(lotsData, "ParkingLot");
            dataAdapterCars.Fill(carsData, "Car");
            dataAdapterRecords.Fill(recordsData, "CarRecords");

            BindingSource bindingSourceLots = new BindingSource();
            bindingSourceLots.DataSource = lotsData;
            bindingSourceLots.DataMember = "ParkingLot";
            dataGridView2.DataSource = bindingSourceLots;

            BindingSource bindingSourceCars = new BindingSource();
            bindingSourceCars.DataSource = carsData;
            bindingSourceCars.DataMember = "Car";
            dataGridView1.DataSource = bindingSourceCars;

            BindingSource bindingSourceRecords = new BindingSource();
            bindingSourceRecords.DataSource = recordsData;
            bindingSourceRecords.DataMember = "CarRecords";
            dataGridView3.DataSource = bindingSourceRecords;
        }

        private void Form1_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            connection.Close();
            timerCars.Stop();
            timerDelete.Stop();
            timerRequests.Stop();
        }

        private void timerRequests_Tick(object sender, EventArgs e)
        {
            SqlCommand addRecord = new SqlCommand("AddRequest", connection);
            addRecord.CommandType = CommandType.StoredProcedure;
            addRecord.ExecuteNonQuery();
            PopulateGrids();
        }

        private void timerDelete_Tick(object sender, EventArgs e)
        {
            SqlCommand deleteCar = new SqlCommand("DeleteCars", connection);
            deleteCar.CommandType = CommandType.StoredProcedure;
            deleteCar.ExecuteNonQuery();
            PopulateGrids();
        }

        private void dataGridView3_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void label4_Click(object sender, EventArgs e)
        {

        }

        private void buttonPause_Click(object sender, EventArgs e)
        {
            timerCars.Stop();
            timerDelete.Stop();
            timerRequests.Stop();
        }

        private void buttonResume_Click(object sender, EventArgs e)
        {
            timerCars.Start();
            timerRequests.Start();
            timerDelete.Start();
        }

        private void buttonStats_Click(object sender, EventArgs e)
        {
            Form2 form2 = new Form2();
            form2.Show();
        }
    }
}
