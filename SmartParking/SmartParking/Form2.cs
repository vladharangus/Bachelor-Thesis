using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Diagnostics;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace SmartParking
{
    public partial class Form2 : Form
    {
        SqlConnection connection;
        DataTable dt;
        SqlDataAdapter da;
        public Form2()
        {
            connection = new SqlConnection(@"Data Source = DESKTOP-1NBNSDK\SQLEXPRESS; Initial Catalog = SmartParking; Integrated Security = true");
            connection.Open();
            InitializeComponent();
        }

        private void Form2_Load(object sender, EventArgs e)
        {
            
        }

        private void button1_Click(object sender, EventArgs e)
        {
            int pid = Convert.ToInt32(textBox1.Text.ToString());
            SqlCommand freeSpots = new SqlCommand("freeSpots", connection);
           freeSpots.CommandType = CommandType.StoredProcedure;

            freeSpots.Parameters.AddWithValue("pid", pid);

            var returnParameter = freeSpots.Parameters.Add("@ReturnVal", SqlDbType.Int);
            returnParameter.Direction = ParameterDirection.ReturnValue;
            freeSpots.ExecuteNonQuery();

            var result = returnParameter.Value;
            label2.Text = result.ToString();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            dt = new DataTable();
            SqlCommand cmd = new SqlCommand("mostOccupied", connection);
            cmd.CommandType = CommandType.StoredProcedure;
            da = new SqlDataAdapter(cmd);
            da.Fill(dt);
            cmd.ExecuteNonQuery();
            dataGridView1.DataSource = dt;
            //dataGridView1.DataBind();
         
        }

        private void button3_Click(object sender, EventArgs e)
        {
            dt = new DataTable();
            int pid = Convert.ToInt32(textBox3.Text.ToString());
            SqlCommand cmd = new SqlCommand("MF", connection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("pid", pid);
            da = new SqlDataAdapter(cmd);
            da.Fill(dt);
            cmd.ExecuteNonQuery();
            dataGridView1.DataSource = dt;
        }

        private void button4_Click(object sender, EventArgs e)
        {
            dt = new DataTable();
            SqlCommand cmd = new SqlCommand("electrical", connection);
            cmd.CommandType = CommandType.StoredProcedure;
            da = new SqlDataAdapter(cmd);
            da.Fill(dt);
            cmd.ExecuteNonQuery();
            dataGridView1.DataSource = dt;
        }

        private void label3_Click(object sender, EventArgs e)
        {

        }

        private void button5_Click(object sender, EventArgs e)
        {
            int cid = Convert.ToInt32(textBox2.Text);
            dt = new DataTable();
            SqlCommand cmd = new SqlCommand("closest", connection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("cid", cid);

            da = new SqlDataAdapter(cmd);
            da.Fill(dt);
            cmd.ExecuteNonQuery();
            dataGridView1.DataSource = dt;
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void button6_Click(object sender, EventArgs e)
        {
            int cid = Convert.ToInt32(textBox2.Text.ToString());
            SqlCommand cmd = new SqlCommand("closestCar", connection);
            cmd.CommandType = CommandType.StoredProcedure;

            cmd.Parameters.AddWithValue("cid", cid);

            var returnParameter = cmd.Parameters.Add("@ReturnVal", SqlDbType.Int);
            returnParameter.Direction = ParameterDirection.ReturnValue;
            cmd.ExecuteNonQuery();

            var result = returnParameter.Value;
            label5.Text = result.ToString();
        }
    }
}
